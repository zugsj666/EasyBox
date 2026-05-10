export type SupportedSubscriptionProtocol =
  | "vless"
  | "vmess"
  | "trojan"
  | "ss"
  | "hysteria2";

export type SubscriptionInputKind = "base64-text" | "uri";

export interface ParseSubscriptionInput {
  kind: SubscriptionInputKind;
  content: string;
  sourceName?: string;
}

export interface ParsedNodeDraft {
  name: string;
  protocol: SupportedSubscriptionProtocol;
  rawConfig: string;
  sourceLabel?: string;
  remoteRemark?: string;
}

export interface ParseSubscriptionResult {
  nodes: ParsedNodeDraft[];
  warnings: string[];
}

export interface SubscriptionParser {
  parse(input: ParseSubscriptionInput): Promise<ParseSubscriptionResult>;
}

