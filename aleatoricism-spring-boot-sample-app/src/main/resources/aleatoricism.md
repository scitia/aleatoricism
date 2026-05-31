# Aleatoric HTTP Router Instructions

## Overview

This file defines the instructions for handling HTTP requests in the Aleatoric framework.
The AI model (running via Copilot SDK) will use these instructions to determine which business operation to execute
based on the HTTP method and path of the incoming request.

## HTTP Methods and Intent

### GET Request
- **Purpose**: Retrieve a resource or data
- **Typical Path Pattern**: `/resource/{id}` or `/resource/list`
- **Expected Behavior**: Query the business logic to fetch data, return the result
- **Tools to Consider**: Query tools, read operations

### POST Request
- **Purpose**: Create a new resource
- **Typical Path Pattern**: `/resource` or `/resource/create`
- **Expected Behavior**: Extract data from request body, pass through business logic for creation
- **Tools to Consider**: Creation tools, initialization tools

### PUT Request
- **Purpose**: Replace/Update an entire resource
- **Typical Path Pattern**: `/resource/{id}` 
- **Expected Behavior**: Extract data from request body, perform full update through business logic
- **Tools to Consider**: Update tools, replacement tools

### PATCH Request
- **Purpose**: Partially update a resource
- **Typical Path Pattern**: `/resource/{id}` or `/resource/{id}/partial`
- **Expected Behavior**: Extract specific fields from request body, perform partial update
- **Tools to Consider**: Partial update tools, patch operations

### DELETE Request
- **Purpose**: Remove/Delete a resource
- **Typical Path Pattern**: `/resource/{id}` or `/resource/{id}/delete`
- **Expected Behavior**: Identify the resource and remove it through business logic
- **Tools to Consider**: Deletion tools, cleanup operations

## Request Context

Each HTTP request will contain:
- **method**: The HTTP method (GET, POST, PUT, PATCH, DELETE)
- **path**: The request URI path
- **queryParams**: URL query parameters (for GET, DELETE)
- **headers**: HTTP headers
- **body**: Request body (for POST, PUT, PATCH)

## Business Logic Integration

The AI model should:
1. Analyze the HTTP method and path
2. Determine which business operation (tool) is most appropriate
3. Extract necessary parameters from the request
4. Execute the tool with proper arguments
5. Return the result as response

## Tool Availability

The following business tools are available (see tool configuration in your project):
- Quote processing tools
- Risk assessment tools
- Pricing calculation tools
- Decision making tools
- Any custom business tools registered in the application

## Error Handling

If the AI model cannot safely execute a request:
- Return a clear error message
- Do not attempt to execute if the intent is unclear
- Always validate that the tool exists and is called with proper arguments

---

**Note**: Modify this file to customize how your application handles different HTTP methods and paths.
Restart the application for changes to take effect.

