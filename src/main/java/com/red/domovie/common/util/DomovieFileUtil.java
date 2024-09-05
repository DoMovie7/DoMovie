package com.red.domovie.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * 사이트 참고<br>
 * https://docs.awspring.io/spring-cloud-aws/docs/3.0.0/reference/html/index.html<br>
 * https://docs.awspring.io/spring-cloud-aws/docs/3.0.0/reference/html/index.html#using-s3-client<br>
 */

@Slf4j
@Component
public class DomovieFileUtil {
	
	/**
	 * 버킷의 이미지를 다른 폴더로 이동할때 사용
	 * <br>
	 * @param s3Client
	 * @param sourceBucket
	 * @param sourceKey
	 * @param destinationBucket
	 * @param destinationKey
	 */
	public String awsS3CopyObject(S3Client s3Client,String sourceBucket,String sourceKey,String destinationBucket,String destinationKey) {
		s3Client.copyObject(CopyObjectRequest.builder()
								//temp의 obj
								.sourceBucket(sourceBucket)
								.sourceKey(sourceKey)// tempKey
								//이동할 정보
								.destinationBucket(destinationBucket)
								.destinationKey(destinationKey)//uploadKey
								.acl(ObjectCannedACL.PUBLIC_READ)//읽기권한부여
								.build());
		return getS3BucketObjectUrl(s3Client, destinationBucket, destinationKey);
	}
	/**
	 * 버킷의 이미지를 삭제할때 사용
	 * <br>
	 * @param s3Client
	 * @param bucket
	 * @param key
	 */
	public void awsS3DeleteObject(S3Client s3Client,String bucket,String key) {
		s3Client.deleteObject(DeleteObjectRequest.builder()
								.bucket(bucket)
								.key(key)//tempKey
								.build());
	}
	
	
	/**
	 * @param s3Client AWS S3Client 객체
	 * @param bucket   S3의 버킷이름
	 * @param key      버킷의 객체(현재는 이미지파일)의 key 
	 * @return  Map 객체에 업로드한 url, key 를 반환
	 */
	public Map<String, String> awsS3fileUpload(MultipartFile multipartFile,S3Client s3Client,String bucket,String key) {
				
		PutObjectRequest putObjectRequest=PutObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.contentType(multipartFile.getContentType())
				.acl(ObjectCannedACL.PUBLIC_READ)
				.build();
		RequestBody requestBody;
		try {
			requestBody = RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());
			//S3 버킷에 이미지 파일 업로드 기능 지원
			s3Client.putObject(putObjectRequest, requestBody);
			log.debug("--->파일업로드 완료!");
		} catch (IOException e) {
			log.debug("error - s3 파일 업로드 처리 오류 : com.green.aws_s3_test.utils.DomovieFileUtil");
		}
		
		
		String url=getS3BucketObjectUrl(s3Client, bucket, key);
		
		Map<String, String> result=new HashMap<>();
		result.put("url", url);
		result.put("key", key);
		
		return result;
	}
	
	
	/**
	 * s3 버킷에 업로드된 이미지의 url을 얻어올때 'https:' 제거후 리턴<br>
	 * 0123456<br>
	 * https://s3.ap-northeast-2.amazonaws.com/nowon.images.host/item/upload/images/f329d825-ec8c-47db-97d2-235c405c8c89.jpg<br>
	 * //s3.ap-northeast-2.amazonaws.com/nowon.images.host/item/upload/images/f329d825-ec8c-47db-97d2-235c405c8c89.jpg<br>
	 * 
	 * @param s3Client
	 * @param bucket
	 * @param key
	 * @return
	 */
	private String getS3BucketObjectUrl(S3Client s3Client,String bucket,String key) {
		return s3Client.utilities()
				.getUrl(obj->obj.bucket(bucket).key(key))
				.toString()
				.substring(6);
	}
	//파일이름확장자 제거 UUID를 이용하여 변경
	public String newFilenameWithoutExtension() {
		return UUID.randomUUID().toString(); //새로운이름을 UUID로 생성
	}
	
	//파일이름 UUID를 이용하여 변경
	private String newFileName(String orgName) {
		int idx=orgName.lastIndexOf(".");
		return UUID.randomUUID().toString() //새로운이름을 UUID로 생성
				+ orgName.substring(idx); //.확장자
	}
	
	//파일이름 nanoTime()을 이용하여 변경
	private String newFileNameByNanotime(String orgName) {
		int idx=orgName.lastIndexOf(".");
		return orgName.substring(0, idx)+"-"+(System.nanoTime()/1000000)
				+ orgName.substring(idx); //.확장자
	}
	
	
	

}
