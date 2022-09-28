package com.blog.domain.response;

/**为了满足wangEditor框架上传成功图片格式的bean
 * */
public class UploadImageSuccessResponse extends UploadImageResponse {
    public int errno = 0;
    public Data data;

    /**@param url 上传的图片url
     * */
    public UploadImageSuccessResponse(String url){
        this.data = new Data(url);
    }
    static class Data{
        public String url;
        public String alt = "";
        public String href = "";

        public Data(String url){
            this.url = url;
        }
    }
}
