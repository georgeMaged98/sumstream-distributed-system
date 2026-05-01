package com.sumstream.sum.grpc.repository;

import com.sumstream.sum.grpc.entity.Sum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SumRepository extends CrudRepository<Sum, Long> {

}
