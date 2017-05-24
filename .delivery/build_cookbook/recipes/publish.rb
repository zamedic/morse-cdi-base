#
# Cookbook Name:: build_cookbook
# Recipe:: publish
#
# Copyright (c) 2017 The Authors, All Rights Reserved.
node.default['coffee-truck']['release']['email'] = 'marc@marcarndt.com'
node.default['coffee-truck']['release']['user'] = 'Marc Arndt'

secrets = get_project_secrets

file '/tmp/gpg.key' do
  content "#{secrets['gpg']}"
end

execute 'gpg2 --import /tmp/gpg.key'

include_recipe 'coffee-truck::publish'