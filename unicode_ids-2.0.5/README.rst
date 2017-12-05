unicode_ids extension
=====================

.. note::

   Japanese version of this document is also available, on the `site
   <http://h12u.com/sphinx/unicode_ids/README_ja.html>`_ or the doc
   folder of this package.

.. role:: fn_rst

Introduction
------------
Currently, June 3, 2015, Sphinx 1.3.1 generates 'id' attributes
in HTML without Non-ASCII characters. This behavior is requirement
of HTML4.01.

But today, these characters can be usable with latest web browsers
and HTML5 specification.

Also, the Sphinx replaces non-ASCII URLs into ASCII only with
imcompatible way against current web standards.

This extension fixes both problems above at every runtime.
The patchs is applied to both docutils and Sphinx to make ideal
behavior.

License
-------
2-clause BSD, same as the Sphinx project.

Installation
------------
You can install or uninstall this package like another Python packages.
Also, you can use this package without installing this package on your Python
systems, the configuration file of Sphinx(:fn_rst:`conf.py`) enable you to use.

System requirements
...................
Tested with 32bit version of Python 2.7.10 and 64bit version of 3.4.3,
both on the Microsoft Windows 8.1 Pro 64bit edition. But with another
versions and on another OSs would be usable.

Python 3 is required if you need full unicode support.
When used with Python 2, the usable character set is limited
with local encoding.

There's a thing important to know: This extension depends on both
docutils 0.12 and Sphinx 1.3.1. The patching is usable *UNTIL* the
some functions keep same as ones on these versions.

How to install
..............
You can install this package as you will do with another one.

#. Open a console and do :code:`pip install unicode_ids`.

   On the MS-Windows,
   :code:`<python_installed_path>\Scripts\pip.exe install unicode_ids`.

#. Or when you get zip archive like :fn_rst:`unicode_ids-2.0.5(.zip)`
   where '2.0.5' is version number,
   change current directiory to the folder that has the zip file,
   and do :code:`pip install unicode_ids-2.0.5.zip`.

   On the MS-Windows,
   :code:`<python_installed_path>\Scripts\pip.exe install unicode_ids-2.0.5.zip`.

#. Or, this way is the Sphinx specific, you can use this package just extracted
   any folder you want. the :fn_rst:`conf.py` enables you to use the themes and
   extensions.

How to use
----------
As another extensions, you can use this extension by editing :fn_rst:`conf.py`.

First, you should add:

.. code-block:: python

  # add 3 lines below
  import distutils.sysconfig
  site_package_path = distutils.sysconfig.get_python_lib()
  sys.path.insert(0, os.path.join(site_package_path, 'sphinxcontrib/unicode_ids'))

Or, when you don't install with pip or like,

.. code-block:: python

  # add just 1 line below
  sys.path.insert(0, '<path_to_the_folder_contains_unicode_ids_py>')

Next, add unicode_ids extension into :code:`extension` list:

.. code-block:: python

   extension = ['unicode_ids', ] # Of course you can add another extensions.

How to know Unicode is acceptable with identifiers
--------------------------------------------------
This section is written at 2015-06-03(JST, UTC+9).

URI general with HTML
.....................
HTML4.01 [HTML401]_ restricts usable characters :code:`A-Za-z0-9_:.`
and hypen `-`. But also recommends how to do with
`B.2.1 Non-ASCII characters in URI attribute values
<http://www.w3.org/TR/html401/appendix/notes.html#h-B.2>`_.

By transforming as told at the section, the URIs consist of
ASCII 7 bit characters only. In the HTML4.01, we should always
UTF-8 to encode/decode URIs, but also be noted some old documents
may expect another local encoding to encode/decode.

HTML5 [HTML5]_ [HTML51]_ specification has the section `2.5 URLs
<http://www.w3.org/TR/html5/infrastructure.html#urls>`_.
The section shows more complex way to determine the encodings.
When the URL is given with local encodings or source documents are
encoded with local encoding, we should use that one instead of UTF-8.

Considering both specifications, we should always make HTML files with
UTF-8 encoded, to make clear percent hexadecimal arrays represent unicode
string transformed by UTF-8. 

There're another standards, W3C URL [W3CURL]_ and 
WHATWG URL Living Standard [WHATWGURL]_ .
They also defines URL code units, URL code points and percent-encoded bytes.
They say the percent-encoded bytes should represent UTF-8 sequences.

Identifiers (anchors) on HTML
.............................
HTML5 defines 'id' attribute(see `3.2.5.1 The id attribute
<http://www.w3.org/TR/html5/dom.html#the-id-attribute>`_) as 
the `unique identifier <http://www.w3.org/TR/html5/infrastructure.html#concept-id>`_.

In the explanation of the word 'DOM' described the
`2.2.2 Dependencies
<http://www.w3.org/TR/html5/infrastructure.html#dependencies>`_
section, you can know 'The concept of an element\'s unique
identifier (ID)' is one of the 'features are defined in the
DOM specification'.

In DOM4 [DOM4]_ , `5.8 Interface Element
<http://www.w3.org/TR/dom#interface-element>`_
defines the 'id' attribute as :code:`DOMString` and the
specification says the `Elements can have an associated unique
identifier (ID) <http://www.w3.org/TR/dom#concept-id>`_.

As described at `9 Historical/9.2 DOM Core
<http://www.w3.org/TR/dom#dom-core>`_, :code:`DOMString` is now
'defined in Web IDL'.

With W3C WebIDL [WebIDL]_ 
at `3.10.15 DOMString
<http://www.w3.org/TR/WebIDL/#idl-DOMString>`_ section,
the DOMString is defined as a sequence of code units.
The `code unit <http://www.w3.org/TR/WebIDL/#dfn-code-unit>`_
is also defined on the WebIDL as a 16 bit unsigned integer,
and is corresponding to UTF-16 encoding.

As shown, we can know the IDs of the HTML elements can be
written with unicode characters. That can be considered
UTF-16 encoded internally. Note that current CSS3 does not
allow starting with digits, two hyphens or a hyphen followed
by a digit(see next section).

Note that DOM3 defines :code:`DOMString` at DOM3CORE [DOM3CORE]_,
see the section `1.2.1 The DOMString Type
<http://www.w3.org/TR/DOM-Level-3-Core/core.html#ID-C74D1578>`_.

Identifiers on CSS
..................
Cascading Style Sheet(CSS) is now level 3. Starts from CSS3, the stability is
defined module by module which are defined CSS 2.1.(see
the `1.1 Introduction <http://www.w3.org/TR/css-2010/#intro>`_ section of
CSS Snapshot 2010 [CSSSnapshot]_ .

On CSS2.1 [CSS21]_ [CSS22]_ `4.1.3 Characters and case
<http://www.w3.org/TR/CSS21/syndata.html#characters>`_ section
shows the set of the characters we can use to define identifiers.
The 2nd paragraph says:

  In CSS, identifiers (including element names, classes, and IDs in selectors)
  can contain only the characters [a-zA-Z0-9] and ISO 10646 characters U+00A0
  and higher, plus the hyphen (-) and the underscore (_); they cannot start
  with a digit, two hyphens, or a hyphen followed by a digit ...(snip)

As shown above, we can use Non-ASCII characters for identifiers. ISO 10646 is
almost same with Unicode. And currently, CSS3 seems to use same definition for
the identifiers.

Identifiers on JavaScript/ECMAScript
....................................
ECMAScript [ECMAScript]_ is the name of global standard of JavaScript, roughly to say :)

In the specification of the ECMAScript, the section `7.6 Identifier Names
and Identifiers <http://www.ecma-international.org/ecma-262/5.1/#sec-7.6>`_
shows usable characters for identifiers.

The section clearly allows use Unicode characters. It seems some character
group are not able to use, but in fact, the rule contains 'Unicode escape
sequence'. This means finally any character we can use.

Related products
----------------
- `sphinx_html5_basic_theme <https://pypi.python.org/pypi/sphinx_html5_basic_theme>`_

Author
------
Suzumizaki-Kimitaka, 2011-2015

History
-------
2.0.5(2015-07-04):

  - Extracted alone from Yogosyu_ extension.
  - First uploaded to PyPI.

2013-12-07:

  Add Python 3 support.

2013-12-06:

  updated to meet Sphinx 1.2.

2011-05-24:

  First release. Included in Yogosyu_ extension.

.. _Yogosyu: https://pypi.python.org/pypi/yogosyu

References
----------
.. [HTML401] `HTML 4.01 <http://www.w3.org/TR/html401/>`_, \
   `1999-12-24REC <http://www.w3.org/TR/1999/REC-html401-19991224/>`_

.. [HTML5] `HTML 5 <http://www.w3.org/TR/html5/>`_, \
   `2014-10-28REC <http://www.w3.org/TR/2014/REC-html5-20141028/>`_ 

.. [HTML51] `HTML 5.1 <http://www.w3.org/TR/html51/>`_, \
   `2015-05-06WD <http://www.w3.org/TR/2015/WD-html51-20150506/>`_

.. [W3CURL] `W3C URL <http://www.w3.org/TR/url/>`_, \
   `2015-12-09WD <http://www.w3.org/TR/2014/WD-url-1-20141209/>`_

.. [WHATWGURL] `WHATWG URL Living Standard <https://url.spec.whatwg.org/>`_

.. [DOM4] `W3C DOM 4 <http://www.w3.org/TR/dom/>`_, \
   `2015-04-28LC <http://www.w3.org/TR/2015/WD-dom-20150428/>`_

.. [WebIDL] `(W3C) WebIDL <http://www.w3.org/TR/WebIDL/>`_, \
   `2012-04-19CR <http://www.w3.org/TR/2012/CR-WebIDL-20120419/>`_

.. [DOM3CORE] `DOM Level 3 Core <http://www.w3.org/TR/DOM-Level-3-Core/>`_, \
   `2004-04-07REC <http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407/>`_

.. [CSSSnapshot] `CSS Snapshot 2010 <http://www.w3.org/TR/css-2010/>`_, \
   `2011-05-12NOTE <http://www.w3.org/TR/2011/NOTE-css-2010-20110512/>`_

.. [CSS21] `CSS 2.1 <http://www.w3.org/TR/CSS2/>`_, \
   `2011-06-07REC <http://www.w3.org/TR/2011/REC-CSS2-20110607/>`_ 

.. [CSS22] `CSS 2.2 <http://dev.w3.org/csswg/css2/>`_, \
   2015-05-28WD(only permalink is broken)

.. [ECMAScript] `ECMAScript 5.1 <http://www.ecma-international.org/ecma-262/5.1/>`_
