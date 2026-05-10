# @easybox/config-builder

Package boundary for building EasyBox client config responses from validated user, device, region, mode, and node context.

The skeleton stage only contains interfaces. It does not implement sing-box config generation.

## Config Response Contract

Config responses must include:

- `configVersion`
- `configHash`
- `expiresAt`
- `singboxConfig`

Clients must request a new config after `expiresAt`.

## Client-Safe Output

`singboxConfig` should contain only runtime fields needed by sing-box. It should not include management metadata such as:

- `sourceId`
- `rawConfig`
- `adminRemark`
- `panelUrl`
- internal database IDs
- subscription source URLs

Runtime connection parameters are still required inside sing-box config. This package boundary is about avoiding extra management metadata, not claiming client-side secrecy.

