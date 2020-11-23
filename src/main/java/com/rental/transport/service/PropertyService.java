package com.rental.transport.service;

import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyService {

    private Set<Property> properties = new HashSet<>();

    @Autowired
    private PropertyRepository repository;

    @Data
    @AllArgsConstructor
    public class Property {

        private String name;
        private String defValue;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Property property = (Property) o;

            return getName().equals(property.getName());
        }

        @Override
        public int hashCode() {
            return getName().hashCode();
        }
    }

    public String getValue(Set<PropertyEntity> entryes, String name) throws ObjectNotFoundException {

        PropertyEntity entry = entryes
                .stream()
                .filter(property -> property.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Property", name));

        return entry.getValue();
    }

    public void setProp(String name, String defValue) {

        properties.add(new PropertyService.Property(name, defValue));
    }

    public void setProps(Set<PropertyEntity> entryes) {

        properties
                .stream()
                .forEach(property -> {
                    String name = property.getName();
                    try {
                        getValue(entryes, name);
                    }
                    catch (ObjectNotFoundException e) {
                        PropertyEntity entity = new PropertyEntity(name, property.getDefValue());
                        repository.save(entity);
                        entryes.add(entity);
                    }
                });
    }
}
