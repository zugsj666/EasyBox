# EasyBox Architecture

EasyBox is a self-hosted system made of an Android client, an API service, a web management panel, and shared packages for node parsing and sing-box config construction.

## Main Components

- Android Client: beginner-focused client that signs in, displays account state, fetches server-provided regions and config, and starts/stops sing-box through Android VPN integration.
- EasyBox API: backend service responsible for authentication, user status, device binding, subscription source syncing, node management, announcements, versions, and config delivery.
- Management Panel: administrator UI for managing users, subscription sources, parsed nodes, announcements, and Android client versions.
- subscription-parser: package boundary for parsing supported subscription text and node URIs into normalized node drafts.
- config-builder: package boundary for building client-safe sing-box runtime config responses from user/device/region/mode/node context.

## Data Flow

The core MVP data flow is:

```text
Android Client -> EasyBox API -> config-builder -> nodes/subscription-parser -> sing-box config
```

Expanded flow:

1. An administrator adds a subscription source in the management panel.
2. EasyBox API fetches the subscription source.
3. `subscription-parser` parses supported base64 subscription text and supported node URIs into node drafts.
4. EasyBox API stores parsed nodes separately from subscription sources.
5. Android Client signs in and binds a device.
6. Android Client requests profile and bootstrap data from EasyBox API.
7. Bootstrap returns server-managed regions, announcements, available modes, and version data.
8. Android Client requests config with `deviceId`, selected region, and selected mode.
9. EasyBox API validates user status, traffic state, expiration, device binding, and available nodes.
10. `config-builder` creates a sing-box runtime config response.
11. Android Client writes temporary runtime config and starts sing-box through the Android VPN flow.

## Important Boundaries

- `node_sources` represent subscription origins only.
- `nodes` represent concrete parsed nodes only.
- Android must not hard-code regions. Regions come from `GET /api/client/bootstrap`.
- Android UI must not display or edit node parameters.
- Runtime sing-box config necessarily contains connection parameters.
- MVP `rawConfig` plaintext storage is acceptable only for trusted self-hosted environments.

