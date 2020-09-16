package com.rental.transport.entity;

import java.util.List;

public interface TransportSettingsRepository extends AbstractRepository<TransportSettingsEntity> {

    List<TransportSettingsEntity> findByTransportId(Long id);
}
