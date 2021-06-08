import { OssProvider } from '@/shared/model/enumerations/oss-provider.model';
export interface IOssConfig {
  id?: number;
  provider?: OssProvider | null;
  ossCode?: string | null;
  endpoint?: string | null;
  accessKey?: string | null;
  secretKey?: string | null;
  bucketName?: string | null;
  appId?: string | null;
  region?: string | null;
  remark?: string | null;
  enabled?: boolean | null;
}

export class OssConfig implements IOssConfig {
  constructor(
    public id?: number,
    public provider?: OssProvider | null,
    public ossCode?: string | null,
    public endpoint?: string | null,
    public accessKey?: string | null,
    public secretKey?: string | null,
    public bucketName?: string | null,
    public appId?: string | null,
    public region?: string | null,
    public remark?: string | null,
    public enabled?: boolean | null
  ) {
    this.enabled = this.enabled ?? false;
  }
}
