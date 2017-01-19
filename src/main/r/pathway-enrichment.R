# The MIT License (MIT)
# Copyright (c) 2017 Dana-Farber Cancer Institute
#
# Permission is hereby granted, free of charge, to any person
# obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without
# restriction, including without limitation the rights to use,
# copy, modify, merge, publish, distribute, sublicense, and/or
# sell copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following
# conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
# OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
# OTHER DEALINGS IN THE SOFTWARE.

# Pathway enrichment
#
# Author: levk

library (org.Hs.eg.db);
GStoEG_map <- as.list (org.Hs.egALIAS2EG);
require(ReactomePA);
EntrezList = lapply (na.omit (genelist), function (GS, GStoEG_map) GStoEG_map[[GS]][1], GStoEG_map = GStoEG_map);
enrichPways <- enrichPathway (gene = EntrezList, minGSSize = minGsSize, organism = organism,
                              pvalueCutoff = pValueCutoff, pAdjustMethod = pAdjustMethod, readable = T);
sum <- summary (enrichPways);
if (is.list (sum)) {
  result <- lapply (sum, function (e) list (description = e$Description,
                                            bgRatio = e$BgRatio,
                                            pValue = e$pvalue,
                                            pAdjust = e$p.adjust,
                                            qValue = e$qvalue,
                                            geneId = e$geneID,
                                            count = e$Count));
  rownames (result) <- sum[, 1];
} else stop (paste ('enrichPathway invalid return value ', paste (as.list (sum))));
