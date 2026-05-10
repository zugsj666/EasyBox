# EasyBox API Design

This document lists the first MVP API surface. It is a design document only; the APIs are not implemented in the skeleton stage.

## Client API

- `POST /api/client/auth/login`
- `POST /api/client/auth/refresh`
- `POST /api/client/auth/logout`
- `GET /api/client/profile`
- `GET /api/client/bootstrap`
- `POST /api/client/config`
- `POST /api/client/device/bind`
- `POST /api/client/traffic/report`
- `GET /api/client/version`

## Admin API

- `POST /api/admin/auth/login`
- `POST /api/admin/auth/logout`
- `GET /api/admin/users`
- `POST /api/admin/users`
- `PATCH /api/admin/users/:id`
- `GET /api/admin/sources`
- `POST /api/admin/sources`
- `PATCH /api/admin/sources/:id`
- `POST /api/admin/sources/:id/sync`
- `GET /api/admin/nodes`
- `PATCH /api/admin/nodes/:id`
- `GET /api/admin/announcements`
- `POST /api/admin/announcements`
- `PATCH /api/admin/announcements/:id`
- `GET /api/admin/client-versions`
- `POST /api/admin/client-versions`
- `PATCH /api/admin/client-versions/:id`

## Config Response

`POST /api/client/config` should return:

```json
{
  "configVersion": "string",
  "configHash": "string",
  "expiresAt": "ISO-8601 timestamp",
  "singboxConfig": {}
}
```

The Android client must request a new config after `expiresAt`.

`singboxConfig` must be the client runtime configuration only. It should not include management fields such as `sourceId`, `rawConfig`, `adminRemark`, or `panelUrl`.

## Bootstrap Response

`GET /api/client/bootstrap` should return server-managed regions. Android must not hard-code region options.

## Device Binding

The first login flow should bind a device. Config requests must include and validate `deviceId`.

The API should support a per-user maximum device count and reject new bindings when the limit is reached.

## Unified Error Codes

- `USER_DISABLED`
- `USER_EXPIRED`
- `TRAFFIC_EXCEEDED`
- `DEVICE_NOT_BOUND`
- `DEVICE_LIMIT_EXCEEDED`
- `NO_AVAILABLE_NODE`
- `CONFIG_EXPIRED`

