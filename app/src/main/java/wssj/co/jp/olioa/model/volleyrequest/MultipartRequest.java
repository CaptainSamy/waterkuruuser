package wssj.co.jp.olioa.model.volleyrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import wssj.co.jp.olioa.model.volleylistener.ReAuthenticationErrorListener;

public class MultipartRequest extends Request<NetworkResponse> {

    private static final String TWO_HYPHENS = "--";

    private static final String LINE_END = "\r\n";

    private final String mBoundary = UUID.randomUUID().toString();

    private Response.Listener<NetworkResponse> mListener;

    private ReAuthenticationErrorListener mErrorListener;

    private Map<String, String> mHeaders;

    /**
     * Default constructor with predefined header and post method.
     *
     * @param url           request destination
     * @param headers       predefined custom header
     * @param listener      on success achieved 200 code from request
     * @param errorListener on error http or library timeout
     */
    public MultipartRequest(String url, Map<String, String> headers,
                            Response.Listener<NetworkResponse> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        //mErrorListener = new ReAuthenticationErrorListener(this, errorListener);
        mHeaders = headers;
    }

    /**
     * Constructor with option method and default header configuration.
     *
     * @param method        method for now accept POST and GET only
     * @param url           request destination
     * @param listener      on success event handler
     * @param errorListener on error event handler
     */
    public MultipartRequest(int method, String url,
                            Response.Listener<NetworkResponse> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        //mErrorListener = new ReAuthenticationErrorListener(this, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + mBoundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // populate data byte payload
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }

            // close multipart form data after text and file data
            dos.writeBytes(TWO_HYPHENS + mBoundary + TWO_HYPHENS + LINE_END);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Custom method handle data payload.
     *
     * @return Map data part label with data byte
     * @throws AuthFailureError
     */
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(
                    response,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    /**
     * Parse string map into data output stream by key and value.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param params           string inputs collection
     * @param encoding         encode the inputs, default UTF-8
     * @throws IOException
     */
    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * Parse data into data output stream.
     *
     * @param dataOutputStream data output stream handle file attachment
     * @param data             loop through data
     * @throws IOException
     */
    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    /**
     * Write string data into header and data output stream.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param parameterName    name of input
     * @param parameterValue   value of input
     * @throws IOException
     */
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + mBoundary + LINE_END);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + LINE_END);
        dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + LINE_END);
        dataOutputStream.writeBytes("Content-Type: application/json; charset=UTF-8" + LINE_END);
        dataOutputStream.writeBytes("Content-Length: " + parameterValue.getBytes().length + LINE_END);
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.write(parameterValue.getBytes());
        dataOutputStream.writeBytes(LINE_END);
    }

    /**
     * Write data file into header and data output stream.
     *
     * @param dataOutputStream data output stream handle data parsing
     * @param dataFile         data byte as DataPart from collection
     * @param inputName        name of data input
     * @throws IOException
     */
    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + mBoundary + LINE_END);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + LINE_END);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + LINE_END);
        }
        dataOutputStream.writeBytes("Content-Length: " + dataFile.getContent().length + LINE_END);
        dataOutputStream.writeBytes(LINE_END);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 4096;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];
        int bytesRead = -1;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.flush();

    }

    /**
     * Simple data container use for passing byte file
     */
    public class DataPart {

        private String mFileName;

        private byte[] mContent;

        private String mType;

        /**
         * Default data part
         */
        public DataPart() {
        }

        /**
         * Constructor with data.
         *
         * @param name label of data
         * @param data byte data
         */
        public DataPart(String name, byte[] data) {
            mFileName = name;
            mContent = data;
        }

        /**
         * Constructor with mime data mType.
         *
         * @param name     label of data
         * @param data     byte data
         * @param mimeType mime data like "image/jpeg"
         */
        public DataPart(String name, byte[] data, String mimeType) {
            mFileName = name;
            mContent = data;
            mType = mimeType;
        }

        /**
         * Getter file name.
         *
         * @return file name
         */
        String getFileName() {
            return mFileName;
        }

        /**
         * Setter file name.
         *
         * @param fileName string file name
         */
        void setFileName(String fileName) {
            mFileName = fileName;
        }

        /**
         * Getter mContent.
         *
         * @return byte file data
         */
        byte[] getContent() {
            return mContent;
        }

        /**
         * Setter mContent.
         *
         * @param content byte file data
         */
        void setContent(byte[] content) {
            mContent = content;
        }

        /**
         * Getter mime mType.
         *
         * @return mime mType
         */
        String getType() {
            return mType;
        }

        /**
         * Setter mime mType.
         *
         * @param mType mime mType
         */
        void setType(String mType) {
            mType = mType;
        }
    }
}

