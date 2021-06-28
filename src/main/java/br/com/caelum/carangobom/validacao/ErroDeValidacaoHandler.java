package br.com.caelum.carangobom.validacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ListaDeErrosOutputDto handle(MethodArgumentNotValidException excecao) {
        List<ErroDeParametroOutputDto> errosList = new ArrayList<>();
        excecao.getBindingResult().getFieldErrors().forEach(e -> {
            ErroDeParametroOutputDto erroDeParametroOutputDto = new ErroDeParametroOutputDto(
                e.getField(), e.getDefaultMessage()
            );
            errosList.add(erroDeParametroOutputDto);
        });
        var errosList2 = new ListaDeErrosOutputDto();
        errosList2.setErros(errosList);
        return errosList2;
    }
}
