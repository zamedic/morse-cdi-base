#
# Cookbook Name:: build_cookbook
# Recipe:: publish
#
# Copyright (c) 2017 The Authors, All Rights Reserved.
node.default['coffee-truck']['release']['email'] = 'marc@marcarndt.com'
node.default['coffee-truck']['release']['user'] = 'Marc Arndt'

include_recipe 'maven_sonatype_truck::publish'