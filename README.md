# EasyBox

EasyBox is an open-source, self-hosted Android proxy client and management panel for beginners and users who do not want to manage node details manually.

The goal is not to build another advanced client with full node editing. EasyBox is designed around a simple user experience: sign in, choose a region if needed, and connect with one tap.

## Project Status

This repository is currently in the skeleton and planning stage.

The first implementation milestone is an Android technical verification demo for:

- Android `VpnService` permission flow
- Foreground Service
- Persistent notification
- Local sing-box config loading
- Start proxy
- Stop proxy
- View logs

Full backend APIs, the management panel, subscription parsing, config generation, and Android UI are intentionally not implemented yet.

## What EasyBox Provides

EasyBox provides self-hosted software:

- Android client
- Backend API
- Web management panel
- Shared type packages
- Subscription parsing package
- sing-box config builder package

EasyBox does not provide:

- Public proxy nodes
- Proxy services
- Subscription services
- Hosted SaaS services

Operators are responsible for their own infrastructure, nodes, subscriptions, policies, and local legal compliance.

## Product Direction

The Android client is intentionally simple:

- Users can sign in and sign out.
- Users can connect and disconnect with one tap.
- Users can choose from server-provided regions.
- Users can use simple modes such as smart mode or global mode.
- Users can see remaining traffic, expiration time, announcements, and version information.

The Android client UI must not show node parameters or provide editing/import/export entry points for node configuration.

Hidden from the client UI:

- Subscription links
- Real node addresses
- UUIDs
- Ports
- SNI
- Reality parameters
- TLS parameters
- Advanced DNS settings
- Route rule editing
- Raw config JSON
- Node import/export

## Security Boundary

UI hiding is not a security boundary.

At runtime, the sing-box configuration must contain the connection parameters required to connect to selected nodes. EasyBox does not promise protection against reverse engineering, rooted devices, memory inspection, local file inspection, packet capture, or other adversarial client-side access.

The MVP may store node `rawConfig` in plaintext on the server side. This is only suitable for a trusted self-hosted environment and early development. Future versions should add encrypted storage for sensitive node configuration.

Client traffic reports in the MVP are only rough statistics and must not be treated as trusted billing data.

## License

EasyBox is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE) and [NOTICE](./NOTICE).

