package com.madewithtea.tracking.views

import com.twitter.finatra.response.Mustache

@Mustache("user")
case class ClientView(site: String, version: String)
