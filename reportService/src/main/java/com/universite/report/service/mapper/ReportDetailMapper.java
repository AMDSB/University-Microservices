package com.universite.report.service.mapper;

import com.universite.report.domain.Report;
import com.universite.report.domain.ReportDetail;
import com.universite.report.service.dto.ReportDTO;
import com.universite.report.service.dto.ReportDetailDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportDetail} and its DTO {@link ReportDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportDetailMapper extends EntityMapper<ReportDetailDTO, ReportDetail> {
    @Mapping(target = "report", source = "report", qualifiedByName = "reportId")
    ReportDetailDTO toDto(ReportDetail s);

    @Named("reportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportDTO toDtoReportId(Report report);
}
