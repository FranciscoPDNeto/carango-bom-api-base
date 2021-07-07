package br.com.caelum.carangobom.config.swagger;

import br.com.caelum.carangobom.usuario.Usuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;

@Configuration
public class SwaggerConfigurations {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.caelum.carangobom"))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(Usuario.class)
                .globalOperationParameters(Arrays.asList(
                    new ParameterBuilder()
                        .name("Authorization")
                        .description("Header para toker JWT. Necessariamente é do tipo Bearer.")
                        .modelRef(new ModelRef("string"))
                        .scalarExample("Bearer <token>")
                        .parameterType("header")
                        .required(false)
                        .build()
                ));
    }

}
