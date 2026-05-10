export type UserStatus = "active" | "disabled" | "expired";

export type DevicePlatform = "android";

export type ClientMode = "smart" | "global";

export type ApiErrorCode =
  | "USER_DISABLED"
  | "USER_EXPIRED"
  | "TRAFFIC_EXCEEDED"
  | "DEVICE_NOT_BOUND"
  | "DEVICE_LIMIT_EXCEEDED"
  | "NO_AVAILABLE_NODE"
  | "CONFIG_EXPIRED";

export interface ApiErrorResponse {
  code: ApiErrorCode;
  message: string;
}

export interface DeviceInfo {
  deviceId: string;
  deviceName?: string;
  platform: DevicePlatform;
  appVersion?: string;
}

export interface Region {
  code: string;
  name: string;
  enabled: boolean;
  sortOrder: number;
}

export interface Announcement {
  id: string;
  title: string;
  content: string;
  priority: number;
  publishedAt: string;
}

export interface ClientVersion {
  platform: DevicePlatform;
  versionName: string;
  versionCode: number;
  downloadUrl?: string;
  changelog?: string;
  forceUpdate: boolean;
}

export interface ClientProfileResponse {
  userId: string;
  username: string;
  status: UserStatus;
  trafficLimitBytes: number;
  trafficUsedBytes: number;
  trafficRemainingBytes: number;
  expireAt: string;
}

export interface ClientBootstrapResponse {
  regions: Region[];
  defaultRegionCode: string;
  modes: ClientMode[];
  defaultMode: ClientMode;
  announcements: Announcement[];
  version?: ClientVersion;
}

export interface ClientConfigRequest {
  deviceId: string;
  regionCode: string;
  mode: ClientMode;
}

export interface ClientConfigResponse {
  configVersion: string;
  configHash: string;
  expiresAt: string;
  singboxConfig: Record<string, unknown>;
}

