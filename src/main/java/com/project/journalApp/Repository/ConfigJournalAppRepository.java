package com.project.journalApp.Repository;

import com.project.journalApp.Entity.ConfigJournalAppEntity;
import com.project.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

}
