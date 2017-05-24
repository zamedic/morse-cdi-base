#
# Cookbook Name:: build_cookbook
# Recipe:: default
#
# Copyright (c) 2017 The Authors, All Rights Reserved.

include_recipe 'coffee-truck::default'

secrets = get_project_secrets

maven_settings "settings.servers" do
  value "server" => {
      "id" => "ossrh",
      "username" => "zamedic",
      "password" => "#{secrets['osspass']}"
  }
end