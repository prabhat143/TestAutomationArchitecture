package com.companyName.utils;


import com.companyName.annotation.FindByWithWait;
import com.companyName.annotation.FindAllWithWait;
import com.companyName.enums.ScrollTypes;
import com.companyName.enums.WaitTypes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import static com.companyName.enums.WaitTypes.*;


public class CustomFieldDecorator implements FieldDecorator {
    private final WebDriver driver;

    public CustomFieldDecorator(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public Object decorate(ClassLoader classLoader, Field field) {
        if (!WebElement.class.isAssignableFrom(field.getType())) {
            if(!List.class.isAssignableFrom(field.getType())) {
                return null;
            }
        }

        ElementLocator locator = getElementLocator(field);
        Object proxy;
        if (List.class.isAssignableFrom(field.getType())) {
            proxy = Proxy.newProxyInstance(classLoader, new Class[]{List.class}, new LazyWebElementListHandler(locator));
        } else {
            proxy = Proxy.newProxyInstance(classLoader, new Class[]{WebElement.class}, new LazyWebElementHandler(locator));
        }

        return proxy;
    }

    private ElementLocator getElementLocator(Field field) {
        FindByWithWait findByWithWait = field.getAnnotation(FindByWithWait.class);
        FindAllWithWait findAllWithWait = field.getAnnotation(FindAllWithWait.class);
        int defaultTimeoutInSeconds = 5;
        int timeoutInSeconds = findByWithWait != null ? findByWithWait.waitTimeoutInSeconds() :
                (findAllWithWait != null ? findAllWithWait.waitTimeoutInSeconds() : defaultTimeoutInSeconds);

        return new CustomElementLocator(driver, field, timeoutInSeconds);
    }

    private static class CustomElementLocator implements ElementLocator {
        private final WebDriver driver;
        private final Field field;
        private final int timeoutInSeconds;

        public CustomElementLocator(WebDriver driver, Field field, int timeoutInSeconds) {
            this.driver = driver;
            this.field = field;
            this.timeoutInSeconds = timeoutInSeconds;
        }
        @Override
            public WebElement findElement() {
            By by = getBy();
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
            FindByWithWait annotation = field.getAnnotation(FindByWithWait.class);
            return new CustomFieldDecorator(driver).scrollWaitGetElement(annotation, wait, by);
            }

            @Override
            public List<WebElement> findElements() {
                By by = getBy();
                WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
                FindAllWithWait annotation = field.getAnnotation(FindAllWithWait.class);
                return new CustomFieldDecorator(driver).scrollWaitGetElements(annotation, wait, by);
            }

        private By getBy() {
            // Check for FindByWithWait xpath locator
            FindByWithWait findByWithWait = field.getAnnotation(FindByWithWait.class);
            if (findByWithWait != null) {
                if (!findByWithWait.xpath().isEmpty()) {
                    return By.xpath(findByWithWait.xpath());
                } else if (!findByWithWait.id().isEmpty()) {
                    return By.id(findByWithWait.id());
                } else if (!findByWithWait.name().isEmpty()) {
                    return By.name(findByWithWait.name());
                } else if (!findByWithWait.className().isEmpty()) {
                    return By.className(findByWithWait.className());
                } else if (!findByWithWait.css().isEmpty()) {
                    return By.cssSelector(findByWithWait.css());
                } else if (!findByWithWait.linkText().isEmpty()) {
                    return By.cssSelector(findByWithWait.linkText());
                } else if (!findByWithWait.partialLinkText().isEmpty()) {
                    return By.cssSelector(findByWithWait.partialLinkText());
                }
            }

            // Check for FindAllWithWait xpath locator
            FindAllWithWait findAllWithWait = field.getAnnotation(FindAllWithWait.class);
            if (findAllWithWait != null) {
                if (!findAllWithWait.xpath().isEmpty()) {
                    return By.xpath(findAllWithWait.xpath());
                } else if (!findAllWithWait.id().isEmpty()) {
                    return By.id(findAllWithWait.id());
                } else if (!findAllWithWait.name().isEmpty()) {
                    return By.name(findAllWithWait.name());
                } else if (!findAllWithWait.className().isEmpty()) {
                    return By.className(findAllWithWait.className());
                } else if (!findAllWithWait.css().isEmpty()) {
                    return By.cssSelector(findAllWithWait.css());
                } else if (!findAllWithWait.linkText().isEmpty()) {
                    return By.cssSelector(findAllWithWait.linkText());
                } else if (!findAllWithWait.partialLinkText().isEmpty()) {
                    return By.cssSelector(findAllWithWait.partialLinkText());
                }
            }

            return null;
        }

    }

    private static class LazyWebElementHandler implements InvocationHandler {
        private final ElementLocator locator;

        public LazyWebElementHandler(ElementLocator locator) {
            this.locator = locator;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            WebElement element = locator.findElement();
            try {
                return method.invoke(element, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private WebElement scrollWaitGetElement(FindByWithWait annotation,WebDriverWait wait,By by) {
        WebElement element = null;
        if (annotation.waitType().equals(VISIBILITY)) {
            scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), annotation);
           element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } else if (annotation.waitType().equals(WaitTypes.PRESENCE_OF_ELEMENT_ON_DOM)) {
           element = scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), annotation);
        } else if (annotation.waitType().equals(PRESENCE_OF_TEXT_GREATER_THAN_ONE)) {
             element = scrollAction(wait.until(ExpectedConditions.presenceOfElementLocated(by)), annotation);
            WebElement finalElement = element;
            wait.until((ExpectedCondition<Boolean>) d -> (finalElement.getText().length() > 1));
        } else if(annotation.waitType().equals(ELEMENT_TO_BE_CLICKABLE)){
            element = scrollAction(wait.until(ExpectedConditions.elementToBeClickable(by)), annotation);
        }else  {
                element = driver.findElement(by);
        }
        return element;
    }

    private WebElement scrollAction(WebElement element, FindByWithWait annotation) {
        ScrollTypes scrollType = annotation.scrollType();
        if (scrollType.equals(ScrollTypes.SCROLL_TO_ELEMENT)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
        } else if (scrollType.equals(ScrollTypes.SCROLL_TO_BOTTOM)) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)", element);
        } else if (scrollType.equals(ScrollTypes.SCROLL_TO_TOP)) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        }
        return element;
    }

    private static class LazyWebElementListHandler implements InvocationHandler {
        private final ElementLocator locator;
        public LazyWebElementListHandler(ElementLocator locator) {
            this.locator = locator;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            List<WebElement> elements = locator.findElements();
            try {
                return method.invoke(elements, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private List<WebElement> scrollWaitGetElements(FindAllWithWait annotation,WebDriverWait wait,By by) {
        List<WebElement> elements = new ArrayList<>();
        if (annotation.waitType().equals(VISIBILITY)) {
            elements.addAll(wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by)));
        } else if (annotation.waitType().equals(PRESENCE_OF_ELEMENT_ON_DOM)) {
            elements.addAll(scrollActions(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)), annotation));
        } else if (annotation.waitType().equals(PRESENCE_OF_TEXT_GREATER_THAN_ONE)) {
            List<WebElement> finalElements = scrollActions(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)), annotation);
            for(WebElement finalElement : finalElements) {
                wait.until((ExpectedCondition<Boolean>) d -> (finalElement.getText().length() > 1));
            }
            elements.addAll(finalElements);
        } else {
            elements.add(driver.findElement(by));
        }
        return elements;
    }

    private List<WebElement> scrollActions(List<WebElement> elements, FindAllWithWait annotation) {
        for(WebElement element : elements) {
            ScrollTypes scrollType = annotation.scrollType();
            if (scrollType.equals(ScrollTypes.SCROLL_TO_ELEMENT)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'})", element);
            } else if (scrollType.equals(ScrollTypes.SCROLL_TO_BOTTOM)) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)", element);
            } else if (scrollType.equals(ScrollTypes.SCROLL_TO_TOP)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            }
        }
        return elements;
    }
}
