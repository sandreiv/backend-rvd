package co.edu.unipamplona.ciadti.rvd.model.repository.projection;

import java.time.LocalDate;

public interface HiringCdpInformationProjection {
    Long getIdCdpTesoreria();
    String getNumeroCdpTesoreria();
    LocalDate getFechaCdpTesoreria();
    String getVigenciaCdpTesoreria();
    String getRubrosCdpTesoreria();
    Long getIdSolicitudCdpRvd();
}
