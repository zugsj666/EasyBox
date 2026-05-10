# EasyBox Security Notes

## UI Hiding Is Not a Security Boundary

EasyBox hides node parameters from the Android client UI to reduce mistakes and keep the product simple.

This is not a security boundary. Runtime sing-box config must contain the parameters needed to connect to nodes. EasyBox does not promise protection against reverse engineering, rooted devices, packet capture, memory inspection, local file inspection, or modified clients.

## Client Traffic Reports Are Not Trusted

MVP client traffic reports are only rough statistics for display and operational reference.

They must not be used as trusted billing data because clients can be modified, blocked, replayed, or made to report inaccurate values.

Future versions should add node-side or server-side traffic accounting before relying on traffic for enforcement or billing.

## rawConfig Plaintext Risk

MVP `nodes.rawConfig` may be stored in plaintext to keep the first implementation simple.

This is suitable only for trusted self-hosted environments. Anyone with database access may be able to read sensitive node configuration.

Future versions should add encrypted storage for `rawConfig`, with key management designed for self-hosted deployments.

## Future Hardening

Future security improvements may include:

- Encrypted `rawConfig` storage
- Node-side traffic statistics
- Server-side traffic aggregation
- Config response signing
- Config version validation
- Short-lived config expiration
- Better device binding controls
- Admin audit logs

