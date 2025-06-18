package com.backend.dashboard_tool.sipocrecords;

import com.backend.dashboard_tool.entity.People.Organization;
import com.backend.dashboard_tool.entity.Process_Data.Result;

public record ResultOrgPair(Result result, Organization organization) {}