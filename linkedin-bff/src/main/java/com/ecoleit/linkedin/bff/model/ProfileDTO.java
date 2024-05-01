package com.ecoleit.linkedin.bff.model;

public record ProfileDTO (Integer id, Integer userId, String currentJobTitle,
                          String industry, String summary, String headline, String website,
                          Integer openForWork) {
}
