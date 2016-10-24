/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.fkparent.pdf.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by marced on 29/09/16.
 */
public abstract class PdfComponent<T extends PdfComponent> {

    private static final float BORDER_WIDTH = 0.2f;
    // Expressed in millimeters
    protected float width;
    protected float height;
    protected float parentOffsetX;
    protected float parentOffsetY;

    // Default border
    protected int border = Rectangle.NO_BORDER;

    protected List<PdfComponent> children = new ArrayList<>();

    public PdfComponent() {
    }

    public float getParentOffsetX() {
        return parentOffsetX;
    }

    public float getParentOffsetY() {
        return parentOffsetY;
    }

    public List<PdfComponent> getChildren() {
        return children;
    }

    public void addChild(PdfComponent child) {
        this.children.add(child);
    }

    // Builder style methods to avoid lengthy constructor argument list
    public T offset(float x, float y) {
        this.parentOffsetX = x;
        this.parentOffsetY = y;
        return (T) this;
    }

    public T size(float x, float y) {
        this.width = x;
        this.height = y;
        return (T) this;
    }

    /**
     * Define border for this component. Combinations are supported, such as
     * Rectangle.TOP + Rectangle.LEFT
     *
     * @param border
     * @return
     */
    public T withBorders(int border) {
        this.border = border;
        return (T) this;
    }

    /**
     * Flattens the tree that this components holds.
     * @return All children as a stream
     */
    public Stream<PdfComponent<?>> flattened() {
        return Stream.concat(
                Stream.of(this),
                getChildren().stream().flatMap(PdfComponent::flattened));
    }

    /**
     * Render a PdfComponent. The upper left corner coordinates as expressed in mm.
     * When actually writing to the canvas, mm units must be converted to points. Also, to coordinate system of a page
     * (0,0) start at the lower left corner.
     *
     * @param document
     * @param writer
     * @param x
     * @param y
     * @throws DocumentException
     */
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {

        // Border rendering is handled here
        drawborder(writer.getDirectContent(), x, y);

        for (PdfComponent child : this.getChildren()) {
            // give children x,y adjusted for this parents offset.
            child.render(document, writer, x + child.getParentOffsetX(), y - child.getParentOffsetY());
        }
    }

    private void drawborder(PdfContentByte canvas, float x, float y) {

        Rectangle rect = new Rectangle(Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y - height),
                Utilities.millimetersToPoints(x + width), Utilities.millimetersToPoints(y));
        rect.setBorder(border);
        rect.setBorderWidth(Utilities.millimetersToPoints(BORDER_WIDTH));
        rect.setBorderColor(BaseColor.BLACK);
        canvas.rectangle(rect);

    }

}
