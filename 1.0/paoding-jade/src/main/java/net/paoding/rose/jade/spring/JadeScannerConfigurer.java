package net.paoding.rose.jade.spring;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.provider.DataAccessProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

import static org.springframework.util.Assert.notNull;

public class JadeScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

    private String basePackage;
    private DataAccessProvider dataAccessProvider;

    private Class<? extends Annotation> annotationClass = DAO.class;

    private ApplicationContext applicationContext;

    private String beanName;

    /**
     * This property lets you set the base package for your mapper interface files.
     * <p/>
     * You can set more than one package by using a semicolon or comma as a separator.
     * <p/>
     * Mappers will be searched for recursively starting in the specified package(s).
     *
     * @param basePackage base package name
     */
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setDataAccessProvider(DataAccessProvider dataAccessProvider) {
        this.dataAccessProvider = dataAccessProvider;
    }

    /**
     * {@inheritDoc}
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * {@inheritDoc}
     */
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * {@inheritDoc}
     */
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }

    /**
     * {@inheritDoc}
     */
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(beanDefinitionRegistry);
        scanner.setBasePackage(this.basePackage);
        scanner.setAnnotationClass(this.annotationClass);
        scanner.setApplicationContext(this.applicationContext);
        scanner.setDataAccessProvider(this.dataAccessProvider);
        scanner.registerFilters();

        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }
}
