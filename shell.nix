{ pkgs ? import <nixpkgs> {} }:
pkgs.mkShell {
  buildInputs = [
    pkgs.graalvm11-ce
    pkgs.sbt
    pkgs.metals # Work-in-progress language server for Scala
  ];
}
