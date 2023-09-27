/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package SOAPConnector.processor;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.util.StandardValidators;


import SoapConnectorCallback.Send_XML_SOAP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"custom,Soap"})
@CapabilityDescription("In progress work of Soap connector to make http requests to Soap API and transmit XML object")
public class SoapConnectorProcessor extends AbstractProcessor {
		
	static String XmlBody="<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
			  "<soap:Body>"+
			   "<NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">"+
			     "<ubiNum>"+500+"</ubiNum>"+
			   "</NumberToWords>"+
			  "</soap:Body>"+
			"</soap:Envelope>";
	
    public static final PropertyDescriptor URL = new PropertyDescriptor
            .Builder().name("URL")
            .displayName("URL")
            .description("URL")
            //.required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue("https://www.dataaccess.com/webservicesserver/NumberConversion.wso")
            .build();
    
    public static final PropertyDescriptor XMLDECLARATION= new PropertyDescriptor
            .Builder().name("XMLDECLARATION")
            .displayName("XMLDECLARATION")
            .description("XMLDECLARATION")
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            .build();
    
    public static final PropertyDescriptor XMLBODY = new PropertyDescriptor
            .Builder().name("XMLBODY")
            .displayName("XMLBODY")
            .description("XMLBODY")
            //.required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .defaultValue(XmlBody)
            .build();

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("SUCCESS")
            .description("SUCCESS")
            .build();
    
    public static final Relationship FAILURE = new Relationship.Builder()
            .name("FAILURE")
            .description("FAILURE")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        descriptors = new ArrayList<>();
        descriptors.add(URL);
        descriptors.add(XMLDECLARATION);
        descriptors.add(XMLBODY);
        descriptors = Collections.unmodifiableList(descriptors);

        relationships = new HashSet<>();
        relationships.add(SUCCESS);
        relationships.add(FAILURE);
        relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {

    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) {
    	
    	getLogger().info("Trigger Soap Connector Processor");

        FlowFile flowFile = session.create() ;
        
        
        Send_XML_SOAP send_XML_SOAP = new Send_XML_SOAP(context.getProperty(URL).getValue(),context.getProperty(XMLDECLARATION).getValue(),context.getProperty(XMLBODY).getValue(),getLogger());
        getLogger().info("URL : "+send_XML_SOAP.url);

        
        
        FlowFile responseFlowFile = session.write(flowFile,send_XML_SOAP);
        
        session.transfer(responseFlowFile,SUCCESS);
    }
}
