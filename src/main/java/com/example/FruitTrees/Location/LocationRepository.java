package com.example.FruitTrees.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface LocationRepository  extends  JpaRepository<Location, Long> {


}
