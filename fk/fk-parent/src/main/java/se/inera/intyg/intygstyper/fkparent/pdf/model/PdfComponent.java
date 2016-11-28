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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Base class for all components of the pdf.
 * <p/>
 * The PdfComponent framework was developed to make it easier to programatically build an (almost) identical copy of a
 * sample FK (xfa) pdf.
 * <p/>
 * The resulting structure is a object-graph where all childrens positions are relative to it's parent.
 * <ul>
 * Good to know:
 * <li>All measurements (offsets, widths, heights etc) in a components is expressed in millimeters to make it easier to
 * define the positioning based on measurements of a physically printed FK pdf.</li>
 * <li>iText itself expects all values to be expressed in points, so conversion is needed when actually rendering a
 * component e.g on a PdfContentByte.</li>
 * <li>To avoid having extensive constructor arguments, all but the core properties of a component are set using a
 * builder pattern.</li>
 * </ul>
 * <p/>
 *
 * @param <T>
 *            The subtype
 */
public abstract class PdfComponent<T extends PdfComponent> {

    private static final float BORDER_WIDTH = 0.2f;

    // All measures are expressed in millimeters
    protected float width;
    protected float height;
    protected float parentOffsetX;
    protected float parentOffsetY;

    // Default border
    protected int border = Rectangle.NO_BORDER;
    protected BaseColor borderColor = BaseColor.BLACK;

    protected List<PdfComponent> children = new ArrayList<>();

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

    /**
     * Define the offset (in mm) from the parents top left corner.
     *
     * @param x
     *            left offset relative to parent
     * @param y
     *            top offset relative to parent
     * @return The modified component instance
     */
    @SuppressWarnings("unchecked")
    public T offset(float x, float y) {
        this.parentOffsetX = x;
        this.parentOffsetY = y;
        return (T) this;
    }

    /**
     * Define the size (in mm) of the component.
     *
     * @param width
     *            width of the component
     * @param height
     *            height of the component
     * @return
     */
    @SuppressWarnings("unchecked")
    public T size(float width, float height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    /**
     * Define border for this component.
     * <p/>
     * Combinations of Rectangle.XXX constants are supported, such as
     * Rectangle.TOP + Rectangle.LEFT. Most common is to use Rectangle.BOX
     *
     * @param border
     *            a combination of Rectangle
     * @return
     */
    @SuppressWarnings("unchecked")
    public T withBorders(int border) {
        this.border = border;
        return (T) this;
    }

    /**
     * Define border with a specific color for this component.
     * <p/>
     * Combinations of Rectangle.XXX constants are supported, such as
     * Rectangle.TOP + Rectangle.LEFT. Most common is to use Rectangle.BOX
     *
     * @param border
     *            a combination of Rectangle
     * @param borderColor
     *            a BaseColor to use
     * @return
     */
    @SuppressWarnings("unchecked")
    public T withBorders(int border, BaseColor borderColor) {
        this.border = border;
        this.borderColor = borderColor;
        return (T) this;
    }

    /**
     * Render a PdfComponent. The upper left corner coordinates as expressed in mm.
     * When actually writing to the canvas, mm units must be converted to points. Also, the coordinate system of an
     * iText page (0,0) actually starts at the lower left corner.
     *
     * @param document
     *            Document to render to
     * @param writer
     *            PdfWriter to use
     * @param x
     *            left starting point
     * @param y
     *            top starting point
     * @throws DocumentException
     */
    public void render(Document document, PdfWriter writer, float x, float y) throws DocumentException {

        // Border rendering is so common so it's handled here in the base class
        drawborder(writer.getDirectContent(), x, y);

        for (PdfComponent child : this.getChildren()) {
            // give children an x,y value that is adjusted for this instance's offset.
            child.render(document, writer, x + child.getParentOffsetX(), y - child.getParentOffsetY());
        }
    }

    /**
     * Draws a border (with the specified borderstyle and color) around the effective bounding box of this component.
     *
     * @param canvas
     *            PdfContentByte to output the border to
     * @param x
     *            left starting point of border
     * @param y
     *            top starting point of border
     */
    private void drawborder(PdfContentByte canvas, float x, float y) {

        Rectangle rect = new Rectangle(Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y - height),
                Utilities.millimetersToPoints(x + width), Utilities.millimetersToPoints(y));
        rect.setBorder(border);
        rect.setBorderWidth(Utilities.millimetersToPoints(BORDER_WIDTH));
        rect.setBorderColor(borderColor);
        canvas.rectangle(rect);

    }

    /**
     * Recursively flattens this component and all children it contains.
     *
     * @return This instance and all it's children as a stream
     */
    public Stream<PdfComponent<?>> flattened() {
        return Stream.concat(
                Stream.of(this),
                getChildren().stream().flatMap(PdfComponent::flattened));
    }
}
