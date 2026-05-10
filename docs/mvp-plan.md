# EasyBox MVP Plan

## Goal

The first MVP should prove the complete self-hosted loop:

1. Administrator creates a user and imports nodes.
2. Android user signs in and binds a device.
3. Android client fetches server-provided bootstrap data and config.
4. Android client starts sing-box with one tap.

Before building the full loop, the first engineering risk to validate is Android `VpnService + sing-box`.

## Development Order

1. Initialize project skeleton.
2. Build Android `VpnService + sing-box` local config technical verification demo.
3. Add backend Prisma data model.
4. Add admin login and user management.
5. Add subscription source syncing and node parsing.
6. Add config-builder sing-box config generation.
7. Add client login/profile/bootstrap/config APIs.
8. Connect Android login and one-tap connection.
9. Build the minimal management panel.
10. Add traffic, expiration, announcements, and version support.
11. Run end-to-end testing.

## Android MVP Pages

The Android MVP should stay compact:

- Login page
- Home page
- Region selection dialog
- Settings page

Regions must be returned by the server through bootstrap. Android must only show server-provided regions.

## Management Panel MVP Pages

The first panel should include only:

- Login
- User management
- Subscription source management
- Node management
- Announcements
- Client versions

Dashboard can be minimal or deferred.

## Config Response

`POST /api/client/config` must return:

- `configVersion`
- `configHash`
- `expiresAt`
- `singboxConfig`

The client must fetch a fresh config after expiration.

## Repair Network Flow

The Android "repair network" button should:

1. Stop sing-box.
2. Close the VPN Service.
3. Clear temporary config.
4. Fetch bootstrap and config again.
5. Restore default `auto` region.
6. Reinitialize connection state.

## Deferred Features

Do not include these in the first MVP:

- Windows, macOS, or iOS clients
- User self-registration
- Online payments
- Invitation codes
- Ticket system
- Multi-tenant SaaS
- Complex admin permissions
- High availability deployment
- Complex node speed testing
- Complex failover scheduling
- Trusted node-side traffic accounting
- Trusted billing system
- Telegram or email notifications
- Clash, v2rayN, or Shadowrocket subscription exports
- Client node editing
- Client subscription imports
- Client config exports
- Client custom DNS
- Client custom routing
- Client display of real node address, port, UUID, SNI, Reality, or TLS parameters

