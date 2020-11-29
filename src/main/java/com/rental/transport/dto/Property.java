package com.rental.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    private String logName;
    private String name;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return getLogName().equals(property.getLogName());
    }

    @Override
    public int hashCode() {
        return getLogName().hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Property{");
        sb.append("logName='").append(logName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
