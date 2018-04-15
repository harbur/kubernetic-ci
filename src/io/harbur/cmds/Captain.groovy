#!/usr/bin/groovy
package io.harbur.cmds

def build() {
  sh "captain build"
}

def test() {
  sh "captain test"
}

def push() {
  sh "captain push"
}