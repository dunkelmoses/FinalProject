package Guardian;

import java.io.Serializable;

/**
 * This class holds the details of an article
 *Author : Hicham Soujae
 * To pass it through activity using intents it is implementing
 * Serializable interface
 * */
class Article implements Serializable {

    private String id;
    private String title;
    private String url;
    private String sectionName;

    /**
     *  Gets the article's ID
     * @return The id of the article
     */
    String getId() {
        return id;
    }

    /**
     * Sets the article's ID
     * @param id An ID number for the article
     */
    void setId(String id) {
        this.id = id;
    }


    /**
     * Gets the article's title
     * @return The title of the article
     */
    String getTitle() {
        return title;
    }

    /**
     * Sets the article's title
     * @param title The title of the article
     */
    void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gets the url for the article
     * @return The article's url
     */
    String getUrl() {
        return url;
    }
    /**
     * Sets the url for the article
     * @return The article's url
     */
    void setUrl(String url) {
        this.url = url;
    }
    /**
     * Gets the section name for the article
     * @return The article's section name
     */
    String getSectionName() {
        return sectionName;
    }
    /**
     * Sets the section name for the article
     * @return The article's section name
     */
    void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }


}
