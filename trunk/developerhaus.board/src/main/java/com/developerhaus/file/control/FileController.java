package com.developerhaus.file.control;

import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.developerhaus.domain.File;
import com.developerhaus.file.service.FileService;

@Controller
@RequestMapping(value = "/file")
public class FileController {

	@Autowired
	FileService fileService;
	
	private @Value("#{resourceProperties['ROOT_DIR']}") String FILE_ROOT_PATH;
	
	@RequestMapping(value = "/download/{fileSeq}", method = RequestMethod.GET)
	public void download(@PathVariable("fileSeq") int fileSeq, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		File file = fileService.get(fileSeq);
		String fileNm = file.getFileNm() + "." + file.getFileExt();
		try {
				
			response.reset();
			String client = request.getHeader("User-Agent");

			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-Description", "JSP Generated Data");
			
			if (client.indexOf("MSIE 5.5") != -1) {
				response.setHeader("Content-Type", "doesn/matter; charset=euc-kr");
				response.setHeader("Content-Disposition", "filename="
						+ new String(fileNm));
			} else if (client.indexOf("Opera") != -1) {
				response.setContentType("application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment; filename="
				 		+ new String(fileNm));
			} else if (client.indexOf("Chrome") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename="
				 		+ new String(fileNm));
			} else if (client.indexOf("Safari") != -1) {
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(fileNm.getBytes("UTF-8"),"UTF-8"));
			} else {
				response.setHeader("Content-Disposition", "attachment; filename="
				 		+ new String(fileNm));
			}
			response.setHeader("Content-Transfer-Encoding", "binary;");
			//response.setHeader("Content-Length", ""	+ vo.getFileSz());
			response.setHeader("Pragma", "no-cache;");
			response.setHeader("Expires", "-1;");
			
			try{
				String filePath = file.getFilePth();
				System.out.println("filepath : "+FILE_ROOT_PATH + filePath);
				FileCopyUtils.copy(new FileInputStream(FILE_ROOT_PATH + filePath), response.getOutputStream());
			} catch(Exception e) {
				
			}			
		} catch (Exception ex) {
			
		}
		
	}
}
