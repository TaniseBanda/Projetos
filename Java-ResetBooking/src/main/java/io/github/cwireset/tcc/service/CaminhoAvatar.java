package io.github.cwireset.tcc.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url= "https://some-random-api.ml/img" , name = "avatar")
public interface CaminhoAvatar {

    @RequestMapping(method = RequestMethod.GET, value = "cat")
    String getAvatar();

}
