package beans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import model.entities.Image;
import model.facades.ImageFacade;
import sun.misc.IOUtils;

@Named(value = "imageBean")
@RequestScoped
public class ImageBean {

    @EJB
    private ImageFacade imageFacade;

    private List<Image> images;

    private Image newImage;

    private Part file;

    public ImageBean() {
    }

    public List<Image> getImages() {
        return imageFacade.findAll();
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String createFile() {
        try {
            newImage = new Image();
            newImage.setPath(file.getSubmittedFileName());
            newImage.setAltText("Image from " + new Date().toString());

            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
            String rootpath = scontext.getRealPath("/");
            rootpath = rootpath.replace("build" + File.separator, "");
            
            File fileImage = new File(rootpath + "images" + File.separator + file.getSubmittedFileName());
            InputStream in = file.getInputStream();
            OutputStream out = new FileOutputStream(fileImage);
            
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            
            in.close();
            out.flush();
            out.close();
            
            imageFacade.create(newImage);
            newImage = new Image();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Image created successfully", ""));
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error occured:", ex.getMessage()));
        }

        return "";
    }

}
