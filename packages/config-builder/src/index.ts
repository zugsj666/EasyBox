export type ConfigBuildMode = "smart" | "global";

export interface ConfigBuildUserContext {
  userId: string;
  status: "active" | "disabled" | "expired";
  trafficLimitBytes: number;
  trafficUsedBytes: number;
  expireAt: string;
}

export interface ConfigBuildDeviceContext {
  deviceId: string;
  platform: "android";
  appVersion?: string;
}

export interface ConfigBuildNode {
  nodeId: string;
  name: string;
  protocol: string;
  regionCode: string;
  weight: number;
  enabled: boolean;
  rawConfig: string;
}

export interface BuildClientConfigInput {
  user: ConfigBuildUserContext;
  device: ConfigBuildDeviceContext;
  regionCode: string;
  mode: ConfigBuildMode;
  availableNodes: ConfigBuildNode[];
  configExpireMinutes: number;
}

export interface BuildClientConfigResult {
  configVersion: string;
  configHash: string;
  expiresAt: string;
  singboxConfig: Record<string, unknown>;
}

export interface ConfigBuilder {
  build(input: BuildClientConfigInput): Promise<BuildClientConfigResult>;
}

