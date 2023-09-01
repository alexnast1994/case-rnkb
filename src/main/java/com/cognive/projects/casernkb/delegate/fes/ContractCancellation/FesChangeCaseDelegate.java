package com.cognive.projects.casernkb.delegate.fes.ContractCancellation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.model.fes.FesCategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FesChangeCaseDelegate implements JavaDelegate {

    private final FesCategoryRepository fesCategoryRepository;
    private final ObjectMapper objectMapper;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var jsonData = delegateExecution.getVariable("payload");
        Long categoryId = (Long) delegateExecution.getVariable("categoryId");
        String json = objectMapper.writeValueAsString(jsonData);
        if(categoryId != null) {
            json = json.replaceAll("\"categoryId\":null", "\"categoryId\":" + categoryId);
        }

        FesCaseSaveDto fesCaseSaveDto = objectMapper.readValue(json, FesCaseSaveDto.class);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true);

        FesCategoryDto fesCategoryDto = fesCaseSaveDto.getFesCategory();
        Provider<FesCategory> fesCategoryProvider = p -> fesCategoryRepository.findById(categoryId).get();
        TypeMap<FesCategoryDto, FesCategory> propertyMapper = modelMapper.createTypeMap(FesCategoryDto.class, FesCategory.class);
        propertyMapper.setProvider(fesCategoryProvider);
        FesCategory fesCategory = modelMapper.map(fesCategoryDto, FesCategory.class);

        fesCategoryRepository.save(fesCategory);

        System.out.println("Кейс сохранен");
    }
}
