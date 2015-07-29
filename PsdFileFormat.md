# Introduction #

This page brings information about top level psd file format descritption. Other pages discuss detailed about each part.


# Details #

Psd file could be divaded to 5 base parts:
  1. Header
  1. Color mode data
  1. Image resources
  1. Layer and Mask
  1. Image data

## Header section ##

Header section has exactly 26 bytes. Contains base information about whole image like image dimensions, number of channels, color depth and color mode.

## Color Mode section ##

First 4 bytes contains length of this section (except this 4 bytes). By Photoshop File Formats document only indexed color and duotone mode have color mode data. Otherwise is length of this section set to 0.

## Image resources section ##

First 4 bytes contains length of this section (except this 4 bytes). Contains metadata information about image. Eaach information starts with **8BIM** signature, follows resource ID, name size of resource data and resource data.

## Layer and Mask section ##

First 4 bytes contains length of this section (except this 4 bytes). This section contains  information about number of layers and detailed information about each layer separately. Layer information is e.g.:
  * top, left, bottom, right coordinates
  * number of channels in layer
  * blend mode information
  * opacity
  * clipping
  * transparency lock and visibility flags
  * and more ...

## Image Data section ##

First 2 bytes means compression method, then image data follows encoded by method. This section contains whole image as should be shown when is rendered from bottom to upper layer and all layer features are applied.

# More informations #

Full Photoshop file format specification is [here](http://www.adobe.com/devnet-apps/photoshop/fileformatashtml/)