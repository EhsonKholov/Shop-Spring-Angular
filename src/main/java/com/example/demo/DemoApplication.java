package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Author : Licf
 * @ Описание: класс конфигурации Swagger2
 * При интеграции с Spring boot поместите его в каталог того же уровня, что и Application.java.
 * Через аннотацию @Configuration позвольте Spring загрузить этот тип конфигурации.
 * Затем используйте аннотацию @ EnableSwagger2, чтобы включить Swagger2.
 * @Date : Created in 17:02 2018/4/12
 * @Modified By : Licf
 */

/**
 @Api: украсить весь класс и описать роль контроллера
 @ApiOperation: описать метод класса или интерфейс
 @ApiParam: описание одного параметра
 @ApiModel: использовать объекты для получения параметров
 @ApiProperty: при получении параметров с объектом, опишите поле объекта
 @ApiResponse: 1 описание ответа HTTP
 @ApiResponses: общее описание ответа HTTP.
 @ApiIgnore: используйте эту аннотацию, чтобы игнорировать этот API
 @ApiError: информация, возвращаемая при возникновении ошибки
 @ApiImplicitParam: параметр запроса
 @ApiImplicitParams: несколько параметров запроса
*/


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
