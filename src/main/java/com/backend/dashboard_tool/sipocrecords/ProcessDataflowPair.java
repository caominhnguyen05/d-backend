package com.backend.dashboard_tool.sipocrecords;

import com.backend.dashboard_tool.entity.Process_Data.Dataflow;
import com.backend.dashboard_tool.entity.Process_Data.ProcessEntity;

public record ProcessDataflowPair(ProcessEntity supplier, Dataflow trigger) {

}
