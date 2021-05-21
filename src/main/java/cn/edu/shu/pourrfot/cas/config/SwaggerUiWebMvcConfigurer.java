package cn.edu.shu.pourrfot.cas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author spencercjh
 */
@Component
public class SwaggerUiWebMvcConfigurer implements WebMvcConfigurer {
  private final String baseUrl;

  public SwaggerUiWebMvcConfigurer(@Value("${springfox.documentation.swagger-ui.base-url:}") String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.any())
      .paths(PathSelectors.any())
      .build();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    final String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
    registry.addResourceHandler(baseUrl + "/swagger-ui/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
      .resourceChain(false);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(baseUrl + "/swagger-ui/")
      .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/cas/api");
  }
}
