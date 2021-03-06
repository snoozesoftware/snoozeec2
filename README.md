Snoozeec2
=========

This is the EC2 system service for Snooze. This service allow to interact with Snooze through an EC2 like API.
Only a subset of methods of the huge EC2 API is supported.

## Supported Actions

`DescribeInstances` 

`DescribeImages`

`RunInstances`

`TerminateInstances`

`RebootInstances`


## Use with libcloud driver

```python
from libcloud.compute.types import Provider
from libcloud.compute.providers import get_driver
import time

EC2_ACCESS_KEY = ""
EC2_SECRET_KEY = ""

Driver = get_driver(Provider.EUCALYPTUS)
conn = Driver(
    EC2_ACCESS_KEY, 
    secret=EC2_SECRET_KEY,
    host="localhost",
    secure=False,
    port=4001,
    path="/")

images = conn.list_images()
print images
sizes = conn.list_sizes()
print sizes
node = conn.create_node(image=images[0], size=sizes[1], ex_mincount = 1, name = "vm")
time.sleep(5)
nodes = conn.list_nodes()
print nodes

```


## Use with Euca2ools

First of all, install euca2ools program on your local machine. In order to use euca2ools you need to define some environment variables, this can be done by storing them in a file, and by sourcing it.

```
# snoozeeuca2oolsrc
export EC2_URL="http://<snoozeec2-address>:<snoozeec2-port>"
export S3_URL=""
export EUCALYPTUS_CERT=""
export EC2_CERT=""
export EC2_PRIVATE_KEY=""
export EC2_ACCESS_KEY=""
export EC2_SECRET_KEY=""
```

Source the file

```
source snoozeeuc2oolsrc
```

You can now start working with euca2ools

```
$) euca-describe-images
```

## Development

Fork the repository
Make your bug fixes or feature additions by following our coding conventions (see the snoozecheckstyle repository)
Send a pull request

## Copyright

Snooze is copyrighted by INRIA and released under the GPL v2 license (see LICENSE.txt). It is registered at the APP (Agence de Protection des Programmes) under the number IDDN.FR.001.100033.000.S.P.2012.000.10000.
