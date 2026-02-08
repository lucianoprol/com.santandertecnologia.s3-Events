package com.santander.s3_events.api.error;

import java.util.List;

public record ErrorResponse(String code, String causes, Integer status, List<String> errors) {
}
