# @easybox/subscription-parser

Package boundary for parsing subscription text and node URIs into normalized EasyBox node drafts.

The skeleton stage only contains interfaces. It does not implement parsing.

## MVP Supported Inputs

The first parser implementation should support:

- base64 subscription text
- `vless://`
- `vmess://`
- `trojan://`
- `ss://`
- `hysteria2://`

## Deferred Inputs

Support for these formats is deferred:

- Clash YAML
- Full sing-box JSON

## Boundary

This package should parse input and return node drafts. It should not write to the database, choose regions automatically, apply admin policy, or generate client configs.

