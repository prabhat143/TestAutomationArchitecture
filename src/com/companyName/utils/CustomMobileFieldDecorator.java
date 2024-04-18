package com.companyName.utils;

import com.companyName.annotation.AndroidByWithWait;
import com.companyName.annotation.iOSByWithWait;
import com.companyName.enums.WaitTypes;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

public class CustomMobileFieldDecorator implements FieldDecorator {
    private final AppiumDriver<MobileElement> driver;
    private final boolean isIOSPlatform;

    public CustomMobileFieldDecorator(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.isIOSPlatform = driver.getCapabilities().getCapability("platformName").toString().equalsIgnoreCase("ios");
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isDecoratableList(field))) {
            return null;
        }

        By by = buildByFromField(field);
        if (by == null) {
            return null;
        }

        if (WebElement.class.isAssignableFrom(field.getType())) {
            return proxyForLocator(loader, field, by);
        } else if (List.class.isAssignableFrom(field.getType())) {
            return proxyForListLocator(loader, field, by);
        }

        return null;
    }

    private boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        // Check if the list is a list of WebElements
        Class<?> listType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        return WebElement.class.isAssignableFrom(listType);
    }

    private By buildByFromField(Field field) {
        if (!isIOSPlatform) {
            AndroidByWithWait androidFindBy = field.getAnnotation(AndroidByWithWait.class);
            if (androidFindBy != null) {
                if (!androidFindBy.xpath().isEmpty()) {
                    return MobileBy.xpath(androidFindBy.xpath());
                }
                if (!androidFindBy.id().isEmpty()) {
                    return MobileBy.id(androidFindBy.id());
                }

            }
        }else {
            iOSByWithWait iosFindBy = field.getAnnotation(iOSByWithWait.class);
            if (iosFindBy != null) {
                if (!iosFindBy.xpath().isEmpty()) {
                    return MobileBy.xpath(iosFindBy.xpath());
                }
                if (!iosFindBy.id().isEmpty()) {
                    return MobileBy.id(iosFindBy.id());
                }
                if (!iosFindBy.accessibilityId().isEmpty()) {
                    return MobileBy.AccessibilityId(iosFindBy.accessibilityId());
                }
            }
        }

        return null;
    }

    private Object proxyForLocator(ClassLoader loader, Field field, final By by) {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("toString".equals(method.getName())) {
                return by.toString();
            }
            WebElement element = getElement(field,by,true);
            try {
                return method.invoke(element, args);
            } catch (InvocationTargetException e) {
                // Unwrap the underlying exception
                throw e.getCause();
            }
        };

        return Proxy.newProxyInstance(loader, new Class[]{WebElement.class}, handler);
    }



    private Object proxyForListLocator(ClassLoader loader, Field field, By by) {
        InvocationHandler handler = (proxy, method, args) -> {
            List<WebElement> elements = getElements(by);
            return method.invoke(elements, args);
        };

        return Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }

    private WebElement getElement(Field field, By by, boolean attemptScroll) {
        WebDriverWait wait;
        int waitTimeoutInSeconds;
        WaitTypes waitType;

        if (!isIOSPlatform) {
            AndroidByWithWait androidAnnotation = field.getAnnotation(AndroidByWithWait.class);
            waitTimeoutInSeconds = androidAnnotation.waitTimeoutInSeconds();
            waitType = androidAnnotation.waitType();
        } else {
            iOSByWithWait iosAnnotation = field.getAnnotation(iOSByWithWait.class);
            waitTimeoutInSeconds = iosAnnotation.waitTimeoutInSeconds();
            waitType = iosAnnotation.waitType();
        }

        wait = new WebDriverWait(driver, waitTimeoutInSeconds);

        try {
            switch (waitType) {
                case VISIBILITY:
                    wait.until(ExpectedConditions.presenceOfElementLocated(by));
                    return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
                case ELEMENT_TO_BE_CLICKABLE:
                    return wait.until(ExpectedConditions.elementToBeClickable(by));
                case PRESENCE_OF_ELEMENT_ON_DOM:
                    return wait.until(ExpectedConditions.presenceOfElementLocated(by));
                case NONE:
                    return driver.findElement(by);
            }
        } catch (Exception e) {
            if (attemptScroll) {
                return scrollDownToElement(by).getElement(field, by, false);
            }
        }

        return null;
    }



    private List<WebElement> getElements(By by) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        } catch (Exception e) {
            scrollDownToElement(by);
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        }
    }

    private CustomMobileFieldDecorator scrollDownToElement(By by) {
        if(!isIOSPlatform) {
            String resourceId = by.toString().split(": ")[1];
            driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable())" +
                            ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))"));
        } else {
            HashMap<String, Object> scrollObject = new HashMap<>();
            scrollObject.put("toVisible", true);
            scrollObject.put("element", driver.findElement(by).getId());
            driver.executeScript("mobile:scroll", scrollObject);
        }
        return this;
    }
}
