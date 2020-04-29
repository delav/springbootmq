package com.delav.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil
{
  private static final Logger LOG_MSG = LoggerFactory.getLogger(LogUtil.class);
  
  public static void messageSend(String message, Object... addtions)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Send#");
    sb.append(message.replaceAll("\n|\r|\t", ""));
    for (int i = 0; (addtions != null) && (i < addtions.length); i++)
    {
      Object addtion = addtions[i];
      if ((addtion instanceof String))
      {
        String s = (String)addtion;
        addtions[i] = s.replaceAll("\n|\r|\t", "");
      }
    }
    LOG_MSG.info(sb.toString(), addtions);
  }
  
  public static void messageReceive(String message, Object... addtions)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Receive#");
    sb.append(message.replaceAll("\n|\r|\t", ""));
    for (int i = 0; (addtions != null) && (i < addtions.length); i++)
    {
      Object addtion = addtions[i];
      if ((addtion instanceof String))
      {
        String s = (String)addtion;
        addtions[i] = s.replaceAll("\n|\r|\t", "");
      }
    }
    LOG_MSG.info(sb.toString(), addtions);
  }
}

