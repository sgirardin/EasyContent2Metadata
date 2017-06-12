package org.alfresco.extension.ec2m.utils;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataListsResolver {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private SearchService searchService;
	private NodeService nodeService;
	private StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");

	public Map<String, String> getValuesByType(String type) throws Exception {
		return getMappingsByConfigurationAndType(type, ConfigurationsEnum.VALUE.toString());
	}

	public Map<String, String> getRegexByType(String type) throws Exception {
		return getMappingsByConfigurationAndType(type, ConfigurationsEnum.REGEX.toString());
	}

	public Map<String, String> getCoordinatesByType(String type) throws Exception {
		return getMappingsByConfigurationAndType(type, ConfigurationsEnum.COORDINATES.toString());
	}

	public Map<String, String> getMappingsByConfigurationAndType(String type, String configuration){
		Map<String, String> mapConfType = new HashMap<>();
		List<NodeRef> datalistValues = getExtractorDatalist();
		String typeStr, configurationStr;
		for (NodeRef nodeRef : datalistValues){
			typeStr = nodeService.getProperty(nodeRef, Constants.PROP_TYPE).toString();
			configurationStr = nodeService.getProperty(nodeRef, Constants.PROP_CONFIGURATION).toString();
			if (typeStr != null && configurationStr != null){
				mapConfType.put(nodeService.getProperty(nodeRef, Constants.PROP_PROPERTY).toString(), nodeService.getProperty(nodeRef, Constants.PROP_VALUE).toString());
			}
		}

		return mapConfType;
	}

	public boolean hasMappings(String type){
		List<NodeRef> datalistValues = getExtractorDatalist();
		for (NodeRef nodeRef : datalistValues) {
			if (nodeService.getProperty(nodeRef, Constants.PROP_TYPE).equals(type)) {
				return true;
			}
		}
		return false;
	}

	public List<NodeRef> getExtractorDatalist(){
		String query ="TYPE:\""+ Constants.EXTRACTOR_MAPPING_TYPE_LIST +"\"";
		ResultSet rs = searchService.query(storeRef, SearchService.LANGUAGE_LUCENE, query);

		List<NodeRef> result = rs.getNodeRefs();
		rs.close();

		return result;
	}

	
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
}