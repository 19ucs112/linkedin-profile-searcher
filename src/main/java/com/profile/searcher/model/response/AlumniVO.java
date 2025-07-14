package com.profile.searcher.model.response;

import java.util.UUID;


public record AlumniVO(UUID id, String title, String fullName, String universityName, String profileHeadLine,
                       String location, String connectionDegree, String company, String profileUrl, String companyUrl,
                       Integer passedOutYear) {

}

