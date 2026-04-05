package org.acme.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@RegisterForReflection
public class ApiResponse<T> {

    private List<T> elements;
    private T data;
    private Integer status;
    private String description;
    private Integer currentPage;
    private Integer totalPages;
    private Integer totalElements;

}
