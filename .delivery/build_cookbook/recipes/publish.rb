#
# Cookbook Name:: build_cookbook
# Recipe:: publish
#
# Copyright (c) 2017 The Authors, All Rights Reserved.
node['coffee-truck']['release']['email'] = 'marc@marcarndt.com'
node['coffee-truck']['release']['user'] = 'Marc Arndt'

include_recipe 'coffee-truck::publish'